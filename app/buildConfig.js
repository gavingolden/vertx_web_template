
var DIRS = {
    output: 'build/classes/main',
    srcRoot: 'src/main',
    webappRoot: 'src/main/webapp'
};
DIRS.sources = {
    templates: DIRS.webappRoot + '/templates',
    staticHtml: DIRS.webappRoot + '/static/html',
    less: DIRS.webappRoot + '/static/less',
    css: DIRS.webappRoot + '/static/css',
    js: DIRS.webappRoot + '/static/js',
    tsx: DIRS.srcRoot + '/tsx'
};

module.exports = {
    DIRS: DIRS
};