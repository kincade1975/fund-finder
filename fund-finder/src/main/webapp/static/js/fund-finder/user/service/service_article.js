angular.module('fundFinder')

	.service('User_ArticleService', function($http) {
		this.getArticles = function() {
			return $http.get('/api/v1/article');
		};
		this.getArticle = function(id) {
			return $http.get('/api/v1/article/' + id);
		};
	});