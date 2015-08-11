angular.module('fundFinder')

	.service('Administrator_ConfigurationService', function($http) {
		this.getCompanyQuestions = function(id) {
			return $http.get('/api/v1/configuration/company');
		};
		this.saveCompanyQuestion = function(resource) {
			return $http.post('/api/v1/configuration/company', resource);
		};
		this.deleteCompanyQuestion = function(id) {
			return $http.delete('/api/v1/configuration/company/' + id);
		};
	});