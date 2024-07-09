import instaloader
from urllib.parse import urlparse


def fetch_instagram_info(post_url):
    try:
        # Initialize Instaloader
        L = instaloader.Instaloader()

        # Parse the URL to extract the username and post short code
        parsed_url = urlparse(post_url)
        path_parts = parsed_url.path.strip('/').split('/')

        print(f"Parsed URL: {parsed_url}")
        print(f"Path parts: {path_parts}")

        # Check if it's a valid Instagram post URL
        if len(path_parts) < 2:
            raise ValueError("Invalid Instagram post URL format.")

        post_shortcode = path_parts[1]
        print(f"Post shortcode: {post_shortcode}")

        # Load post using the short code
        post = instaloader.Post.from_shortcode(L.context, post_shortcode)

        print(f"Post typename: {post.typename}")

        # Prepare the post information to return
        post_info = {
            'username': post.owner_username,
            'date_utc': post.date_utc,
            'caption': post.caption,
            'likes': post.likes,
            'comments': post.comments,
            'type': post.typename,  # This will indicate 'GraphImage', 'GraphVideo', or 'GraphSidecar'
            'post_url': post.url,
            'media_urls': []  # To store all media URLs
        }

        if post.typename == 'GraphImage':
            post_info['media_urls'].append(post.url)
        elif post.typename == 'GraphVideo':
            post_info['media_urls'].append(post.video_url)
        elif post.typename == 'GraphSidecar':  # Carousel post
            for node in post.get_sidecar_nodes():
                if node.is_video:
                    post_info['media_urls'].append(node.video_url)
                else:
                    post_info['media_urls'].append(node.display_url)

        print(f"Post info: {post_info}")
        return post_info

    except Exception as e:
        raise ValueError(f"Error fetching Instagram post information: {str(e)}")

def fetch_instagram_story(username):
    try:
        # Initialize Instaloader
        L = instaloader.Instaloader()

        # Load the profile
        profile = instaloader.Profile.from_username(L.context, username)

        # Get the stories from the profile
        stories = []
        for story in L.get_stories(userids=[profile.userid]):
            for item in story.get_items():
                stories.append(item)

        # Prepare the story information to return
        story_info = {
            'username': profile.username,
            'stories': []
        }

        for item in stories:
            if item.is_video:
                story_info['stories'].append(item.video_url)
            else:
                story_info['stories'].append(item.url)

        print(f"Story info: {story_info}")
        return story_info

    except Exception as e:
        raise ValueError(f"Error fetching Instagram story information: {str(e)}")

def fetch_instagram_profile_pic(username):
    try:
        # Initialize Instaloader
        L = instaloader.Instaloader()

        # Load the profile
        profile = instaloader.Profile.from_username(L.context, username)

        # Prepare the profile picture information to return
        profile_pic_info = {
            'username': profile.username,
            'profile_pic_url': profile.profile_pic_url
        }

        print(f"Profile picture info: {profile_pic_info}")
        return profile_pic_info

    except Exception as e:
        raise ValueError(f"Error fetching Instagram profile picture: {str(e)}")

def get_url_insta(post_url):
    return fetch_instagram_info(post_url)

def get_story_insta(username):
    return fetch_instagram_story(username)

def get_profile_pic_insta(username):
    return fetch_instagram_profile_pic(username)
