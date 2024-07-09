from yt_dlp import YoutubeDL

def get_video_info(video_url):
    ydl_opts = {
        'quiet': True,
        'format': 'bestaudio/bestvideo',
        'noplaylist': True  # To avoid playlist processing
    }

    try:
        with YoutubeDL(ydl_opts) as ydl:
            info_dict = ydl.extract_info(video_url, download=False)

            video_info = {
                'id': info_dict.get('id', None),
                'title': info_dict.get('title', None),
                'description': info_dict.get('description', None),
                'thumbnail': info_dict.get('thumbnail', None),
                'upload_date': info_dict.get('upload_date', None),
                'uploader': info_dict.get('uploader', None),
                'duration': info_dict.get('duration', None),
                'view_count': info_dict.get('view_count', None),
                'like_count': info_dict.get('like_count', None),
                'dislike_count': info_dict.get('dislike_count', None)
            }

            # Collect audio URLs with their formats and file sizes
            audio_urls = []
            for fmt in info_dict.get('formats', []):
                if fmt.get('acodec') != 'none' and fmt.get('vcodec') == 'none':  # Audio-only format
                    audio_format = fmt.get('acodec', '')  # Get the audio codec format
                    if audio_format:
                        audio_url = fmt.get('url')
                        audio_urls.append({
                            'url': audio_url,
                            'format': audio_format
                        })

            # Extract video URLs for each resolution from 144p to 1080p
            video_urls = {}
            resolutions = [144, 240, 360, 480, 720, 1080]
            for res in resolutions:
                for fmt in info_dict.get('formats', []):
                    if fmt.get('vcodec') != 'none' and fmt.get('acodec') == 'none':  # Video-only format
                        height = fmt.get('height')
                        url = fmt.get('url')
                        file_size = fmt.get('filesize')  # File size in bytes
                        if height == res and url and "googlevideo.com/videoplayback" in url:
                            video_urls[f'{res}p'] = {'url': url, 'size': file_size}
                            break

            # Add URLs to video_info
            video_info['audio_urls'] = audio_urls
            video_info['video_urls'] = video_urls

            return video_info
    except Exception as e:
        print(f"Error: {e}")
        return None

def get_url(video_url):
    return get_video_info(video_url)
