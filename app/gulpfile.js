'use strict';

let gulp          = require( 'gulp' ),
    gutil         = require( 'gulp-util' ),
    notify        = require( 'gulp-notify' ),
    gulp_less     = require( 'gulp-less' ),
    path          = require( 'path' ),
    del           = require( 'del' ),
    fs            = require( 'fs' ),
    livereload    = require( 'gulp-livereload' ),
    webpackStream = require( 'webpack-stream' ),
    plumber       = require( 'gulp-plumber' );

let DIRS = require( './buildConfig' ).DIRS;

// gulp.src() with plumbing included
gulp.plumbedSrc = function() {
  return gulp.src.apply( gulp, arguments )
             .pipe( plumber( ( err ) => {
               notify.onError( "ERROR: " + err.plugin )( err ); // growl
               gutil.beep();
             } ) );
};

let webpackConfig = require( './webpack.config.js' ),
    webpackCache  = require( 'webpack' ),
    tasks         = {
      typescript: function typescript() {
        return gulp.plumbedSrc( [DIRS.sources.app] )
                   .pipe( webpackStream( webpackConfig, webpackCache ) )
                   .pipe( gulp.dest( DIRS.directories.js ) )
                   .pipe( livereload() );
      },
      less: function less() {
        return gulp.plumbedSrc( DIRS.directories.less + '/**/*.less', { cwd: path.resolve( './' ) } )
                   .pipe( gulp_less() )
                   .pipe( gulp.dest( DIRS.directories.css ) )
                   .pipe( livereload() );
      },
      css: function css() {
        return gulp.plumbedSrc( DIRS.directories.css + '/**/*.css', {
          cwd: path.resolve( './' ),
          base: DIRS.webappRoot
        } )
                   .pipe( gulp.dest( DIRS.output ) )
                   .pipe( livereload() );
      },
      js: function js() {
        return gulp.plumbedSrc( DIRS.directories.js + '/**/*.js*', {
          cwd: path.resolve( './' ),
          base: DIRS.webappRoot
        } )
                   .pipe( gulp.dest( DIRS.output ) )
                   .pipe( livereload() );
      },
      html: function html() {
        var sources = [DIRS.directories.templates + '/**/*.html', DIRS.directories.staticHtml + '/**/*.html'];
        return gulp.plumbedSrc( sources, { cwd: path.resolve( './' ), base: DIRS.webappRoot } )
                   .pipe( gulp.dest( DIRS.output ) )
                   .pipe( livereload() );
      },
      clean: function clean() {
        return del( [DIRS.output + '/static', DIRS.output + '/templates'] );
      },
      watch: function watch() {
        livereload.listen();
        gulp.watch( DIRS.directories.less + '/**/*.less', tasks.less );
        gulp.watch( DIRS.directories.css + '/**/*.css', tasks.css );
        gulp.watch( DIRS.directories.js + '/**/*.js', tasks.js );
        gulp.watch( DIRS.directories.templates + '/**/*.html', tasks.html );
        gulp.watch( DIRS.directories.tsx + '/**/*.tsx', tasks.typescript );
      }
    };

gulp.task( 'default',
    gulp.series(
        tasks.clean,
        gulp.parallel( tasks.less, tasks.css, tasks.html, tasks.typescript ),
        tasks.js,
        tasks.watch
    )
);