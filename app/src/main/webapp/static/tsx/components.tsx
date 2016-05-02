import React = require("react");

interface TimerProps {
  data: number
}
export class Timer extends React.Component<TimerProps, {}> {

  constructor() {
    super();
    console.log( 'Timer created' );
  }

  static defaultProps: Object = {
    data: 0
  };

  render() {
    return (
        <div>{this.props.data} seconds</div>
    )
  }
}

