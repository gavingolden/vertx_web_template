'use strict';

let gulp       = require( 'gulp' ),
    gutil      = require( 'gulp-util' ),
    notify     = require( 'gulp-notify' ),
    gulp_less  = require( 'gulp-less' ),
    path       = require( 'path' ),
    del        = require( 'del' ),
    fs         = require( 'fs' ),
    livereload = require( 'gulp-livereload' ),
    webpack    = require( 'webpack-stream' );

let DIRS = require( './buildConfig' ).DIRS;

let webpackConfig = require( './webpack.config.js' ),
    tasks         = {
      typescript: function typescript () {
        return gulp.src( [DIRS.sources.tsx + '/main.tsx'] )
                   .on( 'error', gutil.log ) // Don't break on error, just log
                   .pipe( webpack( webpackConfig ) )
                   .pipe( gulp.dest( DIRS.sources.js ) )
                   .pipe( livereload() );
      },
      less:       function less () {
        return gulp.src( DIRS.sources.less + '/**/*.less', { cwd: path.resolve( './' ) } )
                   .pipe( gulp_less() )
                   .on( 'error', gutil.log )
                   // .pipe(concat('all.css'))
                   .pipe( gulp.dest( DIRS.sources.css ) )
                   .pipe( livereload() );
      },
      css:        function css () {
        return gulp.src( DIRS.sources.css + '/**/*.css', { cwd: path.resolve( './' ), base: DIRS.webappRoot } )
                   // .pipe(concat('all.css'))
                   .pipe( gulp.dest( DIRS.output ) )
                   .pipe( livereload() );
      },
      js:         function js () {
        return gulp.src( DIRS.sources.js + '/**/*.js*', { cwd: path.resolve( './' ), base: DIRS.webappRoot } )
                   .pipe( gulp.dest( DIRS.output ) )
                   .pipe( livereload() );
      },
      html:       function html () {
        var sources = [DIRS.sources.templates + '/**/*.html', DIRS.sources.staticHtml + '/**/*.html'];
        return gulp.src( sources, { cwd: path.resolve( './' ), base: DIRS.webappRoot } )
                   .pipe( gulp.dest( DIRS.output ) )
                   .pipe( livereload() );
      },
      clean:      function clean () {
        return del( [DIRS.output + '/static', DIRS.output + '/templates'] );
      },
      watch:      function watch () {
        livereload.listen();
        gulp.watch( DIRS.sources.less + '/**/*.less', tasks.less );
        gulp.watch( DIRS.sources.css + '/**/*.css', tasks.css );
        gulp.watch( DIRS.sources.js + '/**/*.js', tasks.js );
        gulp.watch( DIRS.sources.templates + '/**/*.html', tasks.html );
        gulp.watch( DIRS.sources.tsx + '/**/*.tsx', tasks.typescript );
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