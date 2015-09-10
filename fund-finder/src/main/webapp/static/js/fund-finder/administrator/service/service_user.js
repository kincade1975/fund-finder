angular.module('fundFinder')

	.service('Administrator_UserService', function($http) {
		this.getPage = function(uiGridResource) {
			return $http.post('/api/v1/user/page', uiGridResource);
		}; 
		this.deleteUser = function(id) {
			return $http.delete('/api/v1/user/' + id);
		};
	});