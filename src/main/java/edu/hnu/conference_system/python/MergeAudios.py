from pydub import AudioSegment
import sys
import os

def overlay_audio_files(path):
    # 获取指定目录下的所有 .wav 文件
    wav_files = [f for f in os.listdir(path) if f.endswith('.wav')]

    if not wav_files:
        print("no such file as *.wav")
        return

    # 初始化一个空的 AudioSegment
    combined = AudioSegment.silent(duration=0)

    # 逐个叠加音频文件
    for wav_file in wav_files:
        file_path = os.path.join(path, wav_file)
        audio = AudioSegment.from_wav(file_path)
        if(combined.duration_seconds>audio.duration_seconds):
            combined = combined.overlay(audio)
        else:
            combined= audio.overlay(combined)

    # 输出合并后的音频文件
    output_path = os.path.join(path, "out.wav")
    combined.export(output_path, format='wav')
    print(f"save as: {output_path}")


path = sys.argv[1]
overlay_audio_files(path)