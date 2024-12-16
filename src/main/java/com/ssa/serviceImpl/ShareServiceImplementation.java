package com.ssa.serviceImpl;

import com.ssa.constant.StatusConstants;
import com.ssa.exceptions.DataNotFoundException;
import com.ssa.model.*;
import com.ssa.repository.PostRepository;
import com.ssa.repository.ShareRepository;
import com.ssa.repository.UserRepository;
import com.ssa.request.SharePostRequest;
import com.ssa.response.ApiResponse;
import com.ssa.response.GetAllPostResponse;
import com.ssa.response.SharePostResponse;
import com.ssa.response.UserDto;
import com.ssa.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShareServiceImplementation implements ShareService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ShareRepository shareRepository;

    @Override
    public ResponseEntity<ApiResponse<Object>> sharePost(SharePostRequest sharePostRequest) {
        Post post = postRepository.findById(sharePostRequest.getPostId()).orElseThrow(() -> new DataNotFoundException("Post not found"));

        User user = userRepository.findById(sharePostRequest.getUserId()).orElseThrow(() -> new DataNotFoundException("User not found"));
        Optional<Share> existingShare = shareRepository.findByPostIdAndUserId(post, user);
        if (existingShare.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), "You have already shared this post"));
        }
        Share share = new Share();
        share.setPostId(post);
        share.setUserId(user);
        share.setCommentOnShare(sharePostRequest.getComment());

        Share savedShare = shareRepository.save(share);
        GetAllPostResponse originalPostResponse = mapPostToResponses(post);
        UserDto userRes = mapUserToDTO(user);
        SharePostResponse response = new SharePostResponse(
                savedShare.getId(),
                originalPostResponse,
                userRes,
                savedShare.getCommentOnShare(),
                savedShare.getSharedAt()
        );
        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), response));

    }


    private UserDto mapUserToDTO(User user) {
        UserDto res = new UserDto();
        res.setId(user.getId());
        res.setUsername(user.getUserName());
        return res;
    }

    private GetAllPostResponse mapPostToResponses(Post post) {
        GetAllPostResponse response = new GetAllPostResponse();
        response.setId(post.getId());
        response.setUserId(post.getUserId().getId());
        response.setTitle(post.getTitle());
        response.setDescription(post.getDescription());
        response.setTags(post.getTags().stream().map(Tag::getName).toList());
        response.setLikes(post.getLikes().size());
        response.setComments(post.getComments().size());
        Long shareCount = shareRepository.countSharesByPostId_Id(post.getId());
        post.setShareCount(shareCount);
        response.setShare(post.getShareCount());
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            response.setImages(post.getImage().stream()
                    .map(Images::getImageUrl)
                    .toList());
        }
        response.setCreatedAt(post.getCreatedAt());
        response.setUserName(post.getUserId().getUserName());
        return response;
    }

}
