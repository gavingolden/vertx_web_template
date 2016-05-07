import { Menu, PureComp } from "./components";
import { ReactDOM, $, React } from "./libs";

$( () => {
  ReactDOM.render( <Menu/>, document.getElementById( 'app-home-body' ) );
} );
