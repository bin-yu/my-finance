/*global module:false*/
module.exports = function(grunt) {
	//read the dependencies/devDependencies/peerDependencies in your package.json and load all grunt tasks matching the `grunt-*` pattern
	require('load-grunt-tasks')(grunt);

	// Time how long tasks take. Can help when optimizing build times
	require('time-grunt')(grunt);

	// Project configuration.
	grunt.initConfig({

		source : {
			base : 'src/main',
			js : 'src/main/**/*.js',
			css : 'src/main/styles/**/*.css',
			testjs : 'src/test/**/*.js',
			html : 'src/main/**/*.html'
		},
		// Metadata.
		pkg : grunt.file.readJSON('package.json'),
		banner : '/*! <%= pkg.title || pkg.name %> - v<%= pkg.version %> - ' + '<%= grunt.template.today("yyyy-mm-dd") %>\n' + '<%= pkg.homepage ? "* " + pkg.homepage + "\\n" : "" %>' + '* Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author %>;' + ' Licensed <%= _.pluck(pkg.licenses, "type").join(", ") %> */\n',
		// Task configuration.
		/*concat: {
		 js:{
		 src:['src/main/*.js'],
		 dest:'dist/<%= pkg.name %>.js'
		 },
		 options: {
		 banner: '<%= banner %>',
		 stripBanners: true
		 },

		 },
		 uglify: {
		 options: {
		 banner: '<%= banner %>',
		 mangle:false,
		 compress:{
		 drop_console:true,
		 global_defs: {
		 'DEBUG': false
		 },
		 dead_code: false
		 },
		 beautify:true
		 },
		 dist: {
		 src: '<%= concat.js.dest %>',
		 dest: 'dist/<%= pkg.name %>.min.js'
		 }
		 },*/
		clean : ['dist/**/*.*'],
		wiredep : {
			options : {
			},
			app : {
				src : '<%= source.base %>/index.html',

			}
		},
		useminPrepare : {
			html : '<%= source.base %>/index.html',
			options : {
				dest : 'dist',
				flow : {
					steps : {
						js : ['concat', 'uglifyjs'],
						css : ['concat', 'cssmin']
					},
					post : {}
				}
			}
		},
		filerev : {
			dist : {
				src : ['dist/scripts/*.js', 'dist/styles/*.css', 'dist/styles/fonts/*', 'dist/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}']
			}
		},
		usemin : {
			html : 'dist/index.html',
			css : 'dist/styles/*.css',
			options : {
				assertsDirs : ['dist', 'dist/images']
			}
		},
		copy : {
			dist : {
				files : [{
					expand : true,
					dot : true,
					cwd : '<%= source.base %>',
					dest : 'dist',
					src : ['.htaccess', '*.html', 'views/{,*/}*.html']
				}]
			}
		},
		htmlmin : {
			dist : {
				options : {
					collapseWhitespace : true,
					conservativeCollapse : true,
					collapseBooleanAttributes : true,
					removeCommentsFromCDATA : true,
					removeOptionalTags : true
				},
				files : [{
					expand : true,
					cwd : 'dist',
					src : ['*.html', 'views/{,*/}*.html'],
					dest : 'dist'
				}]
			}
		},
		imagemin : {
			dist : {
				files : [{
					expand : true,
					cwd : '<%= source.base %>/images',
					src : '{,*/}*.{png,jpg,jpeg,gif}',
					dest : 'dist/images'
				}]
			}
		},
		ngmin : {
			dist : {
				files : [{
					expand : true,
					cwd : '.tmp/concat/scripts',
					src : '*.js',
					dest : '.tmp/concat/scripts'
				}]
			}
		},
		jshint : {
			options : {
				curly : true,
				eqeqeq : true,
				immed : true,
				latedef : true,
				newcap : true,
				noarg : true,
				sub : true,
				undef : true,
				unused : true,
				boss : true,
				eqnull : true,
				browser : true,
				globals : {
					jQuery : true,
					DEBUG : false,
					console : false
				}
			},
			gruntfile : {
				src : 'Gruntfile.js'
			},
			js : ['<%= source.js %>', '<%= source.testjs %>']
		},
		/*
		 nodeunit: {
		 files: '<%= source.testjs %>'
		 },*/
		watch : {
			gruntfile : {
				files : '<%= jshint.gruntfile.src %>',
				tasks : ['jshint:gruntfile'],
				options : {
					spawn : false,
					interrupt : true,
				}
			},
			js : {
				files : ['<%= source.js %>', '<%= source.testjs %>'],
				tasks : ['jshint:js'],
				options : {
					livereload : true
				}
			},
			css : {
				files : ['<%= source.css %>'],
				options : {
					livereload : true
				}
			},
			html : {
				files : ['<%= source.html %>'],
				options : {
					livereload : true
				}
			}
		},
		connect : {
			options : {
				port : 9001,
				// Change this to '0.0.0.0' to access the server from outside.
				hostname : 'localhost',
				//        hostname: '0.0.0.0',
				livereload : 35729
			},
			livereload : {
				options : {
					open : true,
					/*
					 base : {
					 path : 'src/main',
					 options : {
					 index : 'index.html'
					 }
					 }*/

					middleware : function(connect) {
						var app = connect();
						app.use('/bower_components', connect.static('./bower_components'));
						app.use('/', connect.static('src/main', {
							index : 'index.html'
						}));
						return [app];
					}
				}
			}/*

			 serv : {
			 app : {
			 options : {
			 base : {
			 path : 'src/main',
			 options : {
			 index : 'index.html'
			 }
			 },
			 port : 9001,
			 open : true,
			 // Change this to '0.0.0.0' to access the server from outside.
			 hostname : 'localhost',
			 //keepalive:true,
			 livereload : 35729
			 }
			 }
			 }*/

		},
		karma : {
			unit : {
				configFile : 'karma.conf.js'
			}
		}
	});

	// These plugins provide necessary tasks.
	/*grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	//grunt.loadNpmTasks('grunt-contrib-nodeunit');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-connect');
	grunt.loadNpmTasks('grunt-karma');*/

	// Default task.
	//grunt.registerTask('default', ['jshint', 'nodeunit', 'concat', 'uglify']);
	grunt.registerTask('build', ['clean', 'imagemin', 'useminPrepare', 'concat', 'ngmin','uglify', 'cssmin', 'filerev', 'copy', 'usemin', 'htmlmin']);
	grunt.registerTask('serv', ['connect', 'watch']);
	grunt.registerTask('test', ['karma']);
};
