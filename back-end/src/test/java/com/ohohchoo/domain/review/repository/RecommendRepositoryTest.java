package com.ohohchoo.domain.review.repository;

import com.ohohchoo.domain.review.Address;
import com.ohohchoo.domain.review.entity.Recommend;
import com.ohohchoo.domain.review.entity.RecommendStatus;
import com.ohohchoo.domain.review.entity.Review;
import com.ohohchoo.domain.user.entity.User;
import com.ohohchoo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class RecommendRepositoryTest {

    @Autowired
    RecommendRepository recommendRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName(" 리뷰 좋아요 테스트")
    @Rollback(false)
    public void 리뷰_좋아요_테스트() throws Exception {
        //given
        Review review = createReview();
        User getUser = review.getUser();
        Recommend recommend = createRecommend(getUser, review);
        //when
        Long savedId = recommendRepository.save(recommend).getId();
        Optional<Recommend> findRecommend = recommendRepository.findById(savedId);
        //then
        assertEquals(savedId, findRecommend.get().getId());
    }


    @Test
    @DisplayName(" 좋아요를 싫어요로 변경 테스트")
    @Rollback(false)
    public void 리뷰_좋아요_싫어요_변경테스트() throws Exception {
        //given
        Review review = createReview();
        User getUser = review.getUser();

        // 좋아요를 누름
        Recommend recommend = createRecommend(getUser, review);
        Long savedId = recommendRepository.save(recommend).getId();

        // 좋아요를 싫어요로 변경
        //when
        Recommend findRecommend = recommendRepository.findById(savedId).get();
        findRecommend.changeDisLike();
        //then
        assertEquals(RecommendStatus.DISLIKE, findRecommend.getStatus());
    }

    @Test
    @DisplayName("유저 식별자, 리뷰식별자에 대한 recommend 객체 가 존재하는지 검증")
    public void 유저식별자_리뷰식별자_좋아요조회() throws Exception {
        //given
        Review review = createReview();
        User user = review.getUser();
        Recommend recommend = createRecommend(user, review);
        Long savedId = recommendRepository.save(recommend).getId();
        //when
        Optional<Recommend> findRecommend = recommendRepository.findByUserIdAndReviewId(user.getId(), review.getId());
        //then
        assertEquals(recommend.getId(), findRecommend.get().getId());
    }

    @Test
    @DisplayName("좋아요_싫어요 수 가져오기")
    public void 리뷰_좋아요_싫어요_수_가져오기()throws Exception {
        //given
        Review review = createReview();
        User user = review.getUser();
        Recommend recommend = createRecommend(user, review);
        Long savedId = recommendRepository.save(recommend).getId();
        //when
        Long likeCnt = recommendRepository.countByReviewIdAndStatus(review.getId(), RecommendStatus.LIKE);
        Long dislikeCnt = recommendRepository.countByReviewIdAndStatus(review.getId(),RecommendStatus.DISLIKE);

        //then
        assertEquals(1L,likeCnt);
        assertEquals(0,dislikeCnt);
    }


    // 생성 메서드
    private Recommend createRecommend(User getUser, Review review) {
        return Recommend
                .builder()
                .user(getUser)
                .review(review)
                .status(RecommendStatus.LIKE)
                .build();
    }

    private Review createReview() {
        Review review = Review.builder()
                .user(createUser())
                .content("테스트용 내용작성")
                .address(new Address("서울시", "강서구"))
                .build();
        reviewRepository.save(review);
        return review;
    }

    // 유저 생성 메서드
    private User createUser() {
        String email = "test@naver.com";
        String nickname = "sun";

        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .gender("male")
                .build();
        userRepository.save(user);
        return user;
    }
}