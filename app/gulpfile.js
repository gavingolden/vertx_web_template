var gulp = require('gulp'),
    gutil = require('gulp-util'),
    notify = require('gulp-notify'),
    less = require('gulp-less'),
    path = require('path'),
    del = require('del'),
    fs = require('fs'),
    livereload = require('gulp-livereload'),
    webpack = require('webpack-stream');

var DIRS = require('./buildConfig').DIRS;


// Use to eventually specify gulp target assets in a separate file
// var assets = JSON.parse(fs.readFileSync('gulp-assets.json'));

gulp.task('typescript', function() {
    return gulp.src(DIRS.sources.tsx + '/main.tsx')
        .pipe(webpack( require('./webpack.config.js') ))
        .on('error', gutil.log) // Don't break on error, just log
        .pipe(gulp.dest(DIRS.sources.js))
        .pipe(livereload());
});

gulp.task('less', function () {
    return gulp.src(DIRS.sources.less + '/**/*.less', {cwd: path.resolve('./')})
        .pipe(less())
        .on('error', gutil.log)
        // .pipe(concat('all.css'))
        .pipe(gulp.dest(DIRS.sources.css))
        .pipe(livereload());
});

gulp.task('css', function () {
    return gulp.src(DIRS.sources.css + '/**/*.css', {cwd: path.resolve('./'), base: DIRS.webappRoot})
        // .pipe(concat('all.css'))
        .pipe(gulp.dest(DIRS.output))
        .pipe(livereload());
});

gulp.task('js', function () {
    return gulp.src(DIRS.sources.js + '/**/*.js', {cwd: path.resolve('./'), base: DIRS.webappRoot})
    // .pipe(concat('all.css'))
        .pipe(gulp.dest(DIRS.output))
        .pipe(livereload());
});

gulp.task('html', function () {
    var sources = [DIRS.sources.templates + '/**/*.html', DIRS.sources.staticHtml + '/**/*.html'];

    return gulp.src(sources, {cwd: path.resolve('./'), base: DIRS.webappRoot})
        .pipe(gulp.dest(DIRS.output))
        .pipe(livereload());
});

gulp.task('clean', function() {
    return del([DIRS.output + '/static', DIRS.output + '/templates']);
});


gulp.task('watch', function () {
    livereload.listen();
    gulp.watch(DIRS.sources.less + '/**/*.less', gulp.series('less'));
    gulp.watch(DIRS.sources.css + '/**/*.css', gulp.series('css'));
    gulp.watch(DIRS.sources.js + '/**/*.js', gulp.series('js'));
    gulp.watch(DIRS.sources.templates + '/**/*.html', gulp.series('html'));
    gulp.watch(DIRS.sources.tsx + '/**/*.tsx', gulp.series('typescript'));
});

gulp.task('default',
    gulp.series('clean',
        gulp.parallel('less', 'css', 'html', 'typescript', 'js'), 'watch')
);