var DEFAULT_COUNTRY='pl';
var DEFAULT_CATEGORY='technology';

var CATEGORIES = ['business', 'entertainment', 'general', 'health', 'science', 'sports', 'technology'];


$(document).ready(function() {
    $('#categories').empty();

    CATEGORIES.forEach(function (element, index, array){
        var linkCat = $('<a href="#">' + element + '</a>');
        linkCat.click(function(event) {
            downloadNews(DEFAULT_COUNTRY, $(event.target).text());

            event.preventDefault();
            return false;
        });

        var list = $('<li></li>');
        list.append(linkCat);
        $('#categories').append(list);
    });

    downloadNews(DEFAULT_COUNTRY, DEFAULT_CATEGORY);
});

function downloadNews(country, category) {
    $.ajax({
        url: window.location.href.split("/")[0] + "/news/" + country + "/" + category
    }).then(function(data) {
           $('#newsContent').empty();

           data.articles.forEach(function (element, index, array){

               var author = (element.author == null ? "" : element.author + " - ") + element.sourceName;

               $('#newsContent').append('<h1 class="mt-4">' + element.title + '</h1>');
               $('#newsContent').append('<p class="lead">by <a href="' + element.articleUrl + '">' + author + '</a></p>');
               $('#newsContent').append('<hr />');
               $('#newsContent').append('<p>Posted on ' + element.date + '</p>');
               $('#newsContent').append('<hr />');
               if(element.imageUrl != null) {
                   $('#newsContent').append('<img style="text-align:center; vertical-align:middle; max-width:900px; max-height:300px;" class="img-fluid rounded" src="' + element.imageUrl + '" alt="">');
                   $('#newsContent').append('<hr />');
               }


               $('#newsContent').append(element.description);

               $('#newsContent').append('<hr />');
           })
    },
    function(jqXHR, textStatus, errorThrown) {
           $('#newsContent').empty();

           $('#newsContent').append('<h1 class="mt-4">Error</h1>');
           $('#newsContent').append('<hr />');
           $('#newsContent').append('<p>' + new Date().toString() + '</p>');
           $('#newsContent').append('<hr />');

           $('#newsContent').append(
               "Error when try get news. <br />"
               + "Status: " + textStatus + "(" + errorThrown + ")<br />"
               + "Error: " + jqXHR.responseText
           );

           $('#newsContent').append('<hr />');

        }
    );
}