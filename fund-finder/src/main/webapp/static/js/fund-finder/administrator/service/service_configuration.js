angular.module('fundFinder')

	.service('Administrator_ConfigurationService', function($http) {
		this.getQuestionTypes = function() {
			return $http.get('/api/v1/configuration/question/types');
		};
		this.getCompanyQuestions = function() {
			return $http.get('/api/v1/configuration/company');
		};
		this.getCompanyQuestion = function(id) {
			return $http.get('/api/v1/configuration/company/' + id);
		};
		this.saveCompanyQuestion = function(resource) {
			return $http.post('/api/v1/configuration/company', resource);
		};
		this.deleteCompanyQuestion = function(id) {
			return $http.delete('/api/v1/configuration/company/' + id);
		};
		this.getCities = function() {
			return $http.get('/api/v1/configuration/cities');
		};
		this.getNkds = function() {
			return $http.get('/api/v1/configuration/nkds');
		};
	});