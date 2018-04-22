const util = require('util');
const exec = util.promisify(require('child_process').exec);
const gulp = require('gulp');
const shell = require('gulp-shell');
const prompt = require('gulp-prompt');

let config;

try {
   config = require('./config.json');
} catch (e) { }

const database = 'kwetter'

const createdb = (username, password, done) => {
    const command = `mysql -u ${username} -p ${password || ''} -e "CREATE DATABASE ${database}"`;

    return gulp.src('').pipe(shell([command], () => {
        console.log('\x1b[32m%s\x1b[0m', 'Database created');

        done();
    }));
}

gulp.task('db:create', (done) => {
    if (config) {
        const {username, password} = config || {};

        return createdb(username, password, done);
    }

    console.log('No configuration file was found, requires prompt for credential');

    return gulp.src('').pipe(prompt.prompt([{
        type: 'input',
        name: 'username',
        message: 'Please enter your mysql root username'
    }, {
        type: 'password',
        name: 'password',
        message: 'Please enter your mysql root password'
    }], function(response) {
        const {username, password} = response;

        createdb(username, password, done);
    }));
});