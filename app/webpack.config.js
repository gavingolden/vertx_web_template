'use strict';

let DIRS    = require( './buildConfig' ).DIRS;

module.exports = {
  entry: {
    app: DIRS.sources.app
  },
  output: {
    path: __dirname + '/dist',
    filename: "[name].js",
    publicpath: '/'
  },
  /**
   * Don't bundle these. They will be provided from a CDN with `<script>`
   */
  externals: {
    jQuery: "jquery",
    React: "react",
    ReactDOM: "react-dom"
  },
  devtool: 'source-map',
  cache: false,
  resolve: {
    // .js is required for react imports.
    // .tsx is for our app entry point.
    // .ts is optional, in case you will be importing any regular ts files.
    extensions: ['', '.js', '.ts', '.tsx', '.css']
  },
  module: {
    loaders: [
      { test: /\.css$/, loader: 'style!css' },
      { test: /\.tsx?$/, loader: 'ts-loader' }
    ]
  }
};
