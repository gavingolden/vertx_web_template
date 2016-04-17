

module.exports = {
    entry: {
        app: "./src/main/tsx/main.tsx"
    },
    output: {
        path: __dirname + '/dist',
        filename: "[name].js",
        publicpath:  '/'
    },
    devtool: 'source-map',
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
