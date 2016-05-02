import React = require("react");

interface IntervalMixinProps {
  onInterval: Function; // Function to be executed every ...
  interval: number; // ...milliseconds
}
interface IntervalMixinState {
  data: any; // Temporarily holds the result of the polling operation
}

/**
 * Provides a wrapper component that polls for data on intervals and passes the
 * result to the child component via the `data` property
 * @param TargetComponent The component to be wrapped
 * @returns {C} A wrapped component
 */
// TODO -- allow async
export let IntervalMixer = ( TargetComponent ) => {
  // Name the class to help intellisense
  return class C extends React.Component<IntervalMixinProps, IntervalMixinState> {

    private intervalRef: number;

    constructor() {
      super();
      this.state = { data: undefined };
    }

    private executeInterval() {
      this.setState( { data: this.props.onInterval() } )
    }

    componentDidMount() {
      this.executeInterval();
      setInterval( this.executeInterval.bind( this ), this.props.interval )
    }

    componentWillUnmount() {
      window.clearInterval( this.intervalRef )
    }

    render() {
      return (
          <TargetComponent data={this.state.data}>{this.props.children}</TargetComponent>
      )
    }
  }
};

