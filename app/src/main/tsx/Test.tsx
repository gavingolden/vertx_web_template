
import * as React from "react";
import * as ReactDOM from "react-dom";

interface HelloWorldProps extends React.Props<any> {
  name: string;
}

class HelloMessage extends React.Component<HelloWorldProps, {}> {
  render() {
    return <div>Hello {this.props.name}</div>;
  }
}

ReactDOM.render(<HelloMessage name="John" />, document.getElementById('output'));
