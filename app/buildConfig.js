var DIRS = {
  output:     './build/classes/main',
  webappRoot: './src/main/webapp'
};

DIRS.directories = {
  templates:  DIRS.webappRoot + '/templates',
  staticHtml: DIRS.webappRoot + '/static/html',
  less:       DIRS.webappRoot + '/static/less',
  css:        DIRS.webappRoot + '/static/css',
  js:         DIRS.webappRoot + '/static/js',
  tsx:        DIRS.webappRoot + '/static/tsx'
};

DIRS.sources = {
  app: DIRS.directories.tsx + "/app.tsx"
};

module.exports = {
  DIRS: DIRS
};