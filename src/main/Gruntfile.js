module.exports = function (grunt) {

    grunt.initConfig({
        connect: {
            server: {
                options: {
                    port: 9000,
                    base: 'webapp/'
                }
            }
        },
        watch: {
            project: {
                files: ['webapp/**/*.js', 'webapp/*.html', 'webapp/**/*.html','webapp/**/*.css'],
                options: {
                    livereload: true
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', ['connect', 'watch']);

};
