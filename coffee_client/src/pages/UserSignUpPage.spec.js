import React from 'react';
import {render,cleanup, fireEvent, waitFor} from '@testing-library/react'
import '@testing-library/jest-dom/extend-expect'
import { UserSignupPage } from './UserSignUpPage';

beforeEach(cleanup);

describe('UserSignUp',()=>{
    describe('layout',()=>{
        it('has a header of Sign Up',()=>{
            const {container} = render(<UserSignupPage/>)
            const header = container.querySelector('h1');
            expect(header).toHaveTextContent('Sign Up');
        });

        it('has a input for Display Name',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const displayNameInput = queryByPlaceholderText('Your display name');
            expect(displayNameInput).toBeInTheDocument();
        });


        it('has a input for Username',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const userNameInput = queryByPlaceholderText('Your username');
            expect(userNameInput).toBeInTheDocument();
        });

        it('has a input for Password',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const passwordInput = queryByPlaceholderText('Your password');
            expect(passwordInput).toBeInTheDocument();
        });

        it('has a password type for Password Field',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const passwordInput = queryByPlaceholderText('Your password');
            expect(passwordInput.type).toBe('password');
        });


        it('has a input for Repeat Password',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const repeatPasswordInput = queryByPlaceholderText('Repeat your password');
            expect(repeatPasswordInput).toBeInTheDocument();
        });

        it('has a password type for Repeat Password Field',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const repeatPasswordInput = queryByPlaceholderText('Repeat your password');
            expect(repeatPasswordInput.type).toBe('password');
        });

        it('has a Submit button',()=>{
            const {container} = render(<UserSignupPage/>)
            const submitButton = container.querySelector('button');
            expect(submitButton).toBeInTheDocument();
        });

    })

    describe('Interactions',()=>{
        const changeEvent = (content)=>{ 
            return{
                target : {
                    value:content
                }   
            }
       };

       const mockAsyncDelayed = () =>{
           return jest.fn().mockImplementation(()=>{
               return new Promise((resolve,reject)=>{
                    setTimeout(()=>{
                        resolve({})
                    },300)
               })
           })
       }
       let displayNameInput, userNameInput, passwordInput, repeatPasswordInput, button;
 
       const setupForSubmit = (props)=>{

        const rendered =  render(<UserSignupPage {...props}/>)

        const {container,queryByPlaceholderText} = rendered;

         displayNameInput = queryByPlaceholderText('Your display name');
         userNameInput = queryByPlaceholderText('Your username');
         passwordInput = queryByPlaceholderText('Your password');
         repeatPasswordInput = queryByPlaceholderText('Repeat your password');
 
        fireEvent.change(displayNameInput,changeEvent('my-display-name'))
        fireEvent.change(userNameInput,changeEvent('my-username'))
        fireEvent.change(passwordInput,changeEvent('my-password'))
        fireEvent.change(repeatPasswordInput,changeEvent('my-password'))
 
         button = container.querySelector('button');
         return rendered;
       }

        it('sets Display Name value to state',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const displayNameInput = queryByPlaceholderText('Your display name');

            fireEvent.change(displayNameInput,changeEvent('my-display-name'))
            expect(displayNameInput).toHaveValue('my-display-name');

        })

        it('sets Username value to state',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const userNameInput = queryByPlaceholderText('Your username');

            fireEvent.change(userNameInput,changeEvent('my-username'))
            expect(userNameInput).toHaveValue('my-username');

        })

        it('sets Password value to state',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const passwordInput = queryByPlaceholderText('Your password');
            fireEvent.change(passwordInput,changeEvent('my-password'))
            expect(passwordInput).toHaveValue('my-password');

        })

        it('sets Repeat Password value to state',()=>{
            const {queryByPlaceholderText} = render(<UserSignupPage/>)
            const repeatPasswordInput = queryByPlaceholderText('Repeat your password');
            fireEvent.change(repeatPasswordInput,changeEvent('my-password'))
            expect(repeatPasswordInput).toHaveValue('my-password');

        })

        it('calls postSignUp when all the fields are validated and actions are provided in props',()=>{
           const actions ={
            postSignUp : jest.fn().mockResolvedValueOnce({})
           }

         setupForSubmit({actions});  

       fireEvent.click(button);
       expect(actions.postSignUp).toHaveBeenCalledTimes(1);
       
        })


        it('does not throw an exception when clicking the button when actions are provided in props',()=>{
        
       setupForSubmit();
       expect(()=>   fireEvent.click(button)).not.toThrowError();
        
         })


        it('calls post with user body when the fields are valid',()=>{
            const actions ={
                postSignUp : jest.fn().mockResolvedValueOnce({})
               }
        
            setupForSubmit({actions});
               const expectedUserObj ={
                   username:'my-username',
                   displayName:'my-display-name',
                   password:'my-password'
               } 

            expect(()=>   fireEvent.click(button)).not.toThrowError(expectedUserObj);
             
              })

              xit('does not allow user to click signup when there is an ongoing api request  ',()=>{
                const actions ={
                    postSignup:mockAsyncDelayed()
                }
               setupForSubmit({actions});
               fireEvent.click(button);
               fireEvent.click(button);
                expect(actions.postSignup).toHaveBeenCalledTimes(1);
            })


              xit('display loader when there is a ongoing api call',()=>{
                  const actions ={
                      postSignup:mockAsyncDelayed()
                  }
                  const {queryByText} = setupForSubmit({actions});
                  fireEvent.click(button);
                  const spinner = queryByText('Loading...');
                  expect(spinner).toBeInTheDocument();
              })
              

            //   it('hides loader after api call finishes',async () => {
            //     const actions ={
            //         postSignup:mockAsyncDelayed()
            //     }
            //     const {queryByText} = setupForSubmit({actions});
            //     fireEvent.click(button);

            //     await waitFor();

            //     const spinner = queryByText('Loading...');
            //     expect(spinner).not.toBeInTheDocument();
            // })


            // it('hides loader after api call finishes with error',async () => {
            //     const actions ={
            //         postSignup:jest.fn().mockImplementation(()=>{
            //             return new Promise((resolve,reject)=>{
            //                  setTimeout(()=>{
            //                      reject({
            //                          response:{data:{}}
            //                      })
            //                  },300)
            //             })
            //         })
            //     }
            //     const {queryByText} = setupForSubmit({actions});
            //     fireEvent.click(button);

            //     await waitFor();

            //     const spinner = queryByText('Loading...');
            //     expect(spinner).not.toBeInTheDocument();
            // })



    })
});




console.error =() =>{}