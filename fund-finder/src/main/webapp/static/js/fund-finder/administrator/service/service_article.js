angular.module('fundFinder')

	.service('Administrator_ArticleService', function($http) {
		this.getPage = function(resource) {
			return $http.post('/api/v1/article/page', resource);
		};
		this.getArticle = function(id) {
			return $http.get('/api/v1/article/' + id);
		};
		this.saveArticle = function(resource) {
			return $http.post('/api/v1/article', resource);
		};
		this.deleteArticle = function(id) {
			return $http.delete('/api/v1/article/' + id);
		};
		this.activateArticle = function(id) {
			return $http.get('/api/v1/article/activate/' + id);
		};
		this.deactivateArticle = function(id) {
			return $http.get('/api/v1/article/deactivate/' + id);
		};
	});