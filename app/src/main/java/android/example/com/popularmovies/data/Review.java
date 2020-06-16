package android.example.com.popularmovies.data;

public class Review {

    private final int filmId;
    private final String reviewId;
    private final String author;
    private final String content;
    private final String url;

    public Review(int filmId, String reviewId, String author, String content, String url) {
        this.filmId = filmId;
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public int getFilmId() { return filmId; }

    public String getReviewId() {
        return reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String toString(){
        return String.format("Review id: %s, Url: %s, Author: %s \nContent: %s",
                reviewId, url, author, content);
    }
}
