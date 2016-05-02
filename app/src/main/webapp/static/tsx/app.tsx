import Components = require("./components");
import Mixins = require("./mixins");
import ReactDOM = require("react-dom");
import React = require("react");
import $ = require("jquery");

let IntervalTimer = Mixins.IntervalMixer( Components.Timer );

let ping = (() => {
  let times = 0;

  return () => {
    return times++;
  }
})();

$(() => {
  ReactDOM.render( <IntervalTimer interval={1000} onInterval={ping}/>, document.getElementById( 'app-home-body' ) );
});
