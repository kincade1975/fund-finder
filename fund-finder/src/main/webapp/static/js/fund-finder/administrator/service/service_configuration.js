angular.module('fundFinder')

	.service('Administrator_ConfigurationService', function($http) {
		this.getQuestionTypes = function() {
			return $http.get('/api/v1/configuration/question/types');
		};
		this.getQuestions = function(entityType) {
			return $http.get('/api/v1/configuration/question?entityType=' + entityType);
		};
		this.getQuestion = function(entityType, id) {
			return $http.get('/api/v1/configuration/question/' + id + '?entityType=' + entityType);
		};
		this.saveQuestion = function(resource) {
			return $http.post('/api/v1/configuration/question', resource);
		};
		this.deleteQuestion = function(id) {
			return $http.delete('/api/v1/configuration/question/' + id);
		};
		this.getCities = function() {
			return $http.get('/api/v1/configuration/cities');
		};
		this.getCounties = function() {
			return $http.get('/api/v1/configuration/counties');
		};
		this.getNkds = function() {
			return $http.get('/api/v1/configuration/nkds');
		};
	});