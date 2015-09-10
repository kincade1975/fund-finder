angular.module('fundFinder')

	.service('Administrator_PasswordService', function($http) {
		this.validateEmail = function(email) {
			return $http.post('/password/validateEmail', email);
		};
		this.forgotPassword = function(email) {
			return $http.post('/password/forgot', email);
		};
		this.validateUuid = function(uuid) {
			return $http.post('/password/validateUuid', uuid);
		};
		this.resetPassword = function(data) {
			return $http.post('/password/reset', data);
		};
	});