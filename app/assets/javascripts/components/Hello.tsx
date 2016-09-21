import * as React from "react";

export interface HelloProps { compiler: string; framework: string; }

export interface HelloState { someInput: string; }

export class Hello extends React.Component<HelloProps, HelloState> {

    state: HelloState;

    constructor(props: HelloProps) {
        super(props);

        this.state = {someInput: props.compiler};

        this.handleChange = this.handleChange.bind(this)
    }

    handleChange(event: any) {
        this.setState({someInput: event.target.value})
    }

    render() {
        return (
            <div>
                <h1>Hello from {this.state.someInput} and {this.props.framework}!</h1>
                <input type="text" value={this.state.someInput} onChange={this.handleChange} />
            </div>
        );
    }
}