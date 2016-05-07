import { AppBar } from "material-ui";
import { React } from "./libs";
import PureRenderMixin = require("react-addons-pure-render-mixin");

//TODO -- remove this after immutable is added to typings repo
/// <reference path="../../../../../node_modules/immutable/dist/immutable.d.ts" />

/**
 * Convenience class that automatically implements {@link ComponentLifecycle#shouldComponentUpdate}
 * 
 * The child class must be pure: It will render the same output given the same state/props
 * 
 * Always use immutable data structures to make this worthwhile.
 */
export class PureRenderComponent<P, S> extends React.Component<P, S> {
  constructor() {
    super();
  }

  shouldComponentUpdate = PureRenderMixin.shouldComponentUpdate.bind( this );
}



export class PureComp extends PureRenderComponent<{}, {name: string}> {

  constructor() {
    super();
    this.state = { name: "Initial" }
  }

  private onChange( event ) {
    this.setState( { name: event.target.value } )
  }

  render() {
    return (
        <input
            onChange={this.onChange.bind(this)}
            value={this.state.name}
        />
    )
  }
}

export const Menu = () => {
  let f = () => "Hey there!";
  return (
      <div>
        {f()}
        <AppBar
            title="Title"
            iconClassNameRight="muidocs-icon-navigation-expand-more"
        />
        <PureComp/>
      </div>
  )
};
