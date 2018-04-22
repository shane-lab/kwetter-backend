const util = require('util');
const exec = util.promisify(require('child_process').exec);
const gulp = require('gulp');
const shell = require('gulp-shell');
const prompt = require('gulp-prompt');
const mysql = require('mysql');

let config;

try {
   config = require('./config.json');
} catch (e) { }

let database = 'kwetter'

const createdb = (user, password, done) => {
    const mysqlPool = mysql.createPool({
        host: process.env.HOST || '127.0.0.1',
        user,
        password,
        multipleStatements: true
    });

    mysqlPool.getConnection((err, connection) => {
        if (err) {
            return done(err);
        }

        connection.query(`DROP DATABASE IF EXISTS ${database}; CREATE DATABASE IF NOT EXISTS ${database}`, (err, results) => {
            connection.release();

            if (err) {
                return done(err);
            }

            mysqlPool.end(done);
        });
    });
}

gulp.task('db:create', done => {
    if (config) {
        if (config['database']) {
            database = config['database'];
        }
        const {username, password} = config;

        if (!username) {
            throw new Error('No mysql root username defined in the configuration file');
        }

        return createdb(username, password, done);
    }

    console.log('No configuration file was found, requires prompt for mysql credentials');

    gulp.src('').pipe(prompt.prompt([{
        type: 'input',
        name: 'username',
        message: 'Please enter your mysql root username'
    }, {
        type: 'password',
        name: 'password',
        message: 'Please enter your mysql root password'
    }], response => {
        const {username, password} = response;

        createdb(username, password, done);
    }));
});