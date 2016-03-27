var gulp = require('gulp'),
    gutil = require('gulp-util'),
    notify = require('gulp-notify'),
    less = require('gulp-less'),
    concat = require('gulp-concat'),
    fs = require('fs'),
    livereload = require('gulp-livereload');


var templates = './templates',
    lessDir = './static/less',
    cssDir = './static/css';

// Use to eventually specify gulp target assets in a separate file
// var assets = JSON.parse(fs.readFileSync('gulp-assets.json'));


gulp.task('css', function () {
    return gulp.src(lessDir + '/*.less')
        .pipe(less().on('error', gutil.log))
        // .pipe(concat('all.css'))
        .pipe(gulp.dest(cssDir))
        .pipe(livereload())
});


gulp.task('watch', function () {
    livereload.listen();
    gulp.watch(lessDir + '/*.less', ['css']);
});

gulp.task('default', ['css', 'watch']);