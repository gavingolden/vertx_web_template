var gulp = require('gulp'),
    gutil = require('gulp-util'),
    notify = require('gulp-notify'),
    less = require('gulp-less'),
    path = require('path'),
    // concat = require('gulp-concat'),
    del = require('del'),
    fs = require('fs'),
    livereload = require('gulp-livereload');


var dirs = {
    output: 'build/classes/main',
    root: 'src/main/webapp'
};
dirs.sources = {
    templates: dirs.root + '/templates',
    staticHtml: dirs.root + '/static/html',
    less: dirs.root + '/static/less',
    css: dirs.root + '/static/css'
};


// Use to eventually specify gulp target assets in a separate file
// var assets = JSON.parse(fs.readFileSync('gulp-assets.json'));


gulp.task('less', function () {
    return gulp.src(dirs.sources.less + '/**/*.less', {cwd: path.resolve('./')})
        .pipe(less().on('error', gutil.log))
        // .pipe(concat('all.css'))
        .pipe(gulp.dest(dirs.sources.css))
        .pipe(livereload());
});

gulp.task('css', function () {
    return gulp.src(dirs.sources.css + '/**/*.css', {cwd: path.resolve('./'), base: dirs.root})
        // .pipe(concat('all.css'))
        .pipe(gulp.dest(dirs.output))
        .pipe(livereload());
});

gulp.task('html', function () {
    var sources = [dirs.sources.templates + '/**/*.html', dirs.sources.staticHtml + '/**/*.html'];

    return gulp.src(sources, {cwd: path.resolve('./'), base: dirs.root})
        .pipe(gulp.dest(dirs.output))
        .pipe(livereload());
});

gulp.task('clean', function() {
    return del([dirs.output + '/static', dirs.output + '/templates']);
});


gulp.task('watch', function () {
    livereload.listen();
    gulp.watch(dirs.sources.less + '/**/*.less', ['less']);
    gulp.watch(dirs.sources.css + '/**/*.css', ['css']);
    gulp.watch(dirs.sources.templates + '/**/*.html', ['html']);
});

gulp.task('default', ['clean', 'less', 'css', 'html', 'watch']);