import requests
import json
import os

def fetchVideoAndAudioUrls(link):
    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
        'Accept-Language': 'en-US,en;q=0.9',
        'Dnt': '1',
        'Dpr': '1.3125',
        'Priority': 'u=0, i',
        'Sec-Ch-Prefers-Color-Scheme': 'dark',
        'Sec-Ch-Ua': '"Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"',
        'Sec-Ch-Ua-Full-Version-List': '"Chromium";v="124.0.6367.156", "Google Chrome";v="124.0.6367.156", "Not-A.Brand";v="99.0.0.0"',
        'Sec-Ch-Ua-Mobile': '?0',
        'Sec-Ch-Ua-Model': '""',
        'Sec-Ch-Ua-Platform': '"Windows"',
        'Sec-Ch-Ua-Platform-Version': '"15.0.0"',
        'Sec-Fetch-Dest': 'document',
        'Sec-Fetch-Mode': 'navigate',
        'Sec-Fetch-Site': 'none',
        'Sec-Fetch-User': '?1',
        'Upgrade-Insecure-Requests': '1',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36',
        'Viewport-Width': '1463'
    }
    try:
        resp = requests.get(link, headers=headers)
    except:
        print("Failed to open {}".format(link))
        return None, None
    link = resp.url.split('?')[0]
    resp = resp.text
    splits = link.split('/')
    video_id = ''
    for ids in splits:
        if ids.isdigit():
            video_id = ids
    try:
        target_video_audio_id = resp.split('"id":"{}"'.format(video_id))[1].split(
            '"dash_prefetch_experimental":[')[1].split(']')[0].strip()
    except:
        target_video_audio_id = resp.split('"video_id":"{}"'.format(video_id))[1].split(
            '"dash_prefetch_experimental":[')[1].split(']')[0].strip()
    list_str = "[{}]".format(target_video_audio_id)
    sources = json.loads(list_str)
    video_link = resp.split('"representation_id":"{}"'.format(sources[0]))[
        1].split('"base_url":"')[1].split('"')[0]
    video_link = video_link.replace('\\', '')
    audio_link = resp.split('"representation_id":"{}"'.format(sources[1]))[
        1].split('"base_url":"')[1].split('"')[0]
    audio_link = audio_link.replace('\\', '')

    return video_link, audio_link

def get_video_url(link):
    video_url, audio_url = fetchVideoAndAudioUrls(link)
    return {"video_url": video_url, "audio_url": audio_url}
