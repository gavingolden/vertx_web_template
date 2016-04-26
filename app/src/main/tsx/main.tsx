import * as React from "react";
import * as ReactDOM from "react-dom";
import ClassAttributes = __React.ClassAttributes;
import FormEvent = __React.FormEvent;

interface HelloWorldProps extends ClassAttributes<any> {
  name: string;
}

interface HelloWorldState {
  value: string
}

class HelloMessage extends React.Component<HelloWorldProps, HelloWorldState> {

  constructor ( props: HelloWorldProps, context ) {
    super( props, context );
    this.state = {
      value: 'I have. messasges. for you'
    };
  }

  private ctls: {
    elm?: HTMLInputElement;
  } = {};

  handleChange ( event ) {
    this.setState( {value: event.target.value} )
  }

  render () {
    return (
        <div>Hello {this.props.name}
          <input
              ref={(elm) => this.ctls.elm = elm}
              value={this.state.value}
              onChange={this.handleChange.bind(this)}
          />
        </div>
    )
  }
}

ReactDOM.render(<HelloMessage name="Gavin"/>, document.getElementById( 'app-home-body' ) );

