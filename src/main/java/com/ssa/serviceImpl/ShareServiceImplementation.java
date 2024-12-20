package com.ssa.serviceImpl;

import com.ssa.constant.StatusConstants;
import com.ssa.exceptions.DataNotFoundException;
import com.ssa.model.*;
import com.ssa.repository.PostRepository;
import com.ssa.repository.ShareRepository;
import com.ssa.repository.UserRepository;
import com.ssa.request.SharePostRequest;
import com.ssa.response.*;
import com.ssa.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public ApiResponse<PagedResponse<SharePostResponse>> getAllSharedPostsByUser(Long userId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy == null ? "sharedAt" : sortBy));

        Page<Share> sharedPosts = shareRepository.findAllByUserId_Id(userId, pageable);

        List<SharePostResponse> sharedPostResponses = sharedPosts.getContent().stream()
                .map(this::mapSharedPostToResponse)
                .toList();
        PagedResponse<SharePostResponse> pagedResponse = new PagedResponse<>(
                sharedPostResponses,
                sharedPosts.getNumber(),
                sharedPosts.getSize(),
                sharedPosts.getTotalElements(),
                sharedPosts.getTotalPages(),
                sharedPosts.isLast()
        );

        return new ApiResponse<>(StatusConstants.success(), pagedResponse);
    }

    @Override
    public Page<ShareResponse> getSharedUserOfPost(Long postId, int page, int size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("Post not found with ID: " + postId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Share> shares = shareRepository.findByPostId(post, pageable);

        return shares.map(share -> new ShareResponse(
                share.getUserId().getId(),
                share.getUserId().getUserName(),
                share.getSharedAt()
        ));
    }

    private SharePostResponse mapSharedPostToResponse(Share share) {
        Post originalPost = share.getPostId();
        User sharedByUser = share.getUserId();
        SharePostResponse response = new SharePostResponse();
        response.setId(originalPost.getId());
        response.setOriginalPost(mapPostToResponses(originalPost));
        response.setSharedBy(mapUserToDTO(sharedByUser));
        response.setComment(share.getCommentOnShare());
        response.setSharedAt(share.getSharedAt());

        return response;
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
