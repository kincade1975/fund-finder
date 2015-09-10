angular.module('fundFinder')

	.service('Administrator_RegistrationService', function($http) {
		this.getRegistrationResource = function() {
			return $http.get('/registration');
		};
		this.validateEmail = function(email) {
			return $http.post('/registration/validate-email', email);
		};
		this.register = function(resource) {
			return $http.post('/registration/register', resource);
		};
	});