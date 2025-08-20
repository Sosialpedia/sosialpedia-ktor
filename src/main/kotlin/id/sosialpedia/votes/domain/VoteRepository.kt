package id.sosialpedia.votes.domain

/**
 * Interface untuk mengelola operasi vote (like/dislike).
 */
interface VoteRepository {
    /**
     * Menambahkan, mengubah, atau menghapus vote dari seorang user pada sebuah post atau comment.
     *
     * @param userId ID user yang memberikan vote.
     * @param postId ID post yang divote (nullable jika vote untuk comment).
     * @param commentId ID comment yang divote (nullable jika vote untuk post).
     * @param voteType Tipe vote (1 untuk like, -1 untuk dislike).
     * @return Result yang menandakan apakah operasi berhasil.
     */
    suspend fun addOrUpdateVote(
        userId: String,
        postId: String?,
        commentId: String?,
        voteType: Short
    ): Result<Boolean>
}