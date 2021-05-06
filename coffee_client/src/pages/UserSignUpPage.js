import React from 'react'

export class UserSignupPage extends React.Component{

    state ={
    displayName:"",
    username:"",
    password:"",
    loader:false

    }

    onDisplayNameChange=(event)=>{
        const value = event.target.value;
        this.setState({
            displayName:value
        })
    }


    onUserNameChange=(event)=>{
        const value = event.target.value;
        this.setState({
            username:value
        })
    }


    onPasswordChange=(event)=>{
        const value = event.target.value;
        this.setState({
            password:value
        })
    }

    onClick =()=>{
        const user ={
            username:this.state.username,
            displayName:this.state.displayName,
            password:this.state.password
        }
        this.setState({loader:true})
        this.props.actions.postSignUp(user).then((response)=>{

            this.setState({
                loader:false
            })

        }).catch((error)=>{
            
                this.setState({loader:false})
        })
    }

    render(){
        return(
            <div className="container">
                <h1 className="text-center">Sign Up</h1>
                <div className = "col-12 mb-3">
                <input className="form-control" placeholder="Your display name"  value={this.state.displayName} onChange= {this.onDisplayNameChange}/>
            </div>

            <div className = "col-12 mb-3">
                <input   className="form-control" placeholder="Your username" value={this.state.username} onChange= {this.onUserNameChange} />
            </div>

            <div className = "col-12 mb-3">
                <input  className="form-control"  placeholder="Your password" type="password" value={this.state.password} onChange= {this.onPasswordChange} />
            </div>

            <div className = "col-12 mb-3">
                <input  className="form-control" placeholder="Repeat your password" type="password" />
            </div>

            <div className = "text-center">
                <button className = "btn btn-primary" onClick={this.onClick} 
                disabled={this.state.loader}>
               
           {  this.state.loader &&  ( <div className="spinner-border text-light spinner-border-sm mr-sm-1" role="status">
                <span className="sr-only">Loading...</span>
              </div>)}

                
                Sign Up</button>
            </div>
            </div>  
        );
    }
}

UserSignupPage.defaultProps = {
    actions:{
        postSignUp:()=> new Promise((resolve,reject)=>{
            resolve({});
        })
    }
};

export default UserSignupPage;