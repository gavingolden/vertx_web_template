var gulp = require('gulp'),
    gutil = require('gulp-util'),
    notify = require('gulp-notify'),
    less = require('gulp-less'),
    livereload = require('gulp-livereload');

var resources = './src/main/resources/static',
    templates = './src/main/resources/templates',
    staticHtml = resources + '/html',
    lessDir = resources + '/less',
    targetCSSDir = resources + '/css';

gulp.task('css', function () {
    return gulp.src(lessDir + '/*.less')
        .pipe(less().on('error', gutil.log))
        .pipe(gulp.dest(targetCSSDir))
        //.pipe(notify('CSS minified'))
        .pipe(livereload())
});

gulp.task('templates', function () {
   return gulp.src(templates + '/*.html')
       //.pipe(notify('Templates reloaded'))
       .pipe(livereload())
});

gulp.task('html', function () {
    return gulp.src(staticHtml + '/*.html')
        //.pipe(notify('HTML reloaded'))
        .pipe(livereload())
});


gulp.task('watch', function () {
    livereload.listen();
    gulp.watch(lessDir + '/*.less', ['css']);
    gulp.watch(templates + '/*.html', ['templates']);
    gulp.watch(staticHtml + '/*.html', ['html']);
});

// What tasks does running gulp trigger?
gulp.task('default', ['css', 'templates', 'html', 'watch']);