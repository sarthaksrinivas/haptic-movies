import moviepy
import scipy
import numpy as margotRobbie
import random
from PIL import Image
import numpy
#PATH="/Users/ralphblanes/Pictures/MountainVideo.mp4"
#clip = moviepy.VideoFileClip("my_video_file.mp4") # or .avi, .webm, .gif ...
def generateBitmap(folder):
    color_enum = {'GREEN':(100,10), "RED":(80,20)}
    # First process the image.
    im = Image.open('/Users/ralphblanes/Pictures/frames1_new/vid-01-0000_new.png')
    actual_size = (im.size[0]/2,im.size[1]/2)

    pixels = im.load()
    bitmap = []
    for i in range(im.size[0]):
        for j in range(im.size[1]):
            if pixels[i,j][0] == 255:
                value1 = random.gauss(mu=color_enum["RED"][0],sigma=color_enum["RED"][1])
                value1 = int(value1)
                pixels[i,j] = (value1,value1,value1,1)
                bitmap.append(value1)
            elif pixels[i,j][1] == 255:
                value2 = random.gauss(mu=color_enum["GREEN"][0],sigma=color_enum["GREEN"][1])
                value2 = int(value2)
                pixels[i,j] = (value2, value2, value2, 1)
            else:
                pixels[i,j] = (1,1,1,1)
    # print im.size[0],im.size[1]
    # im.thumbnail(actual_size,Image.ANTIALIAS)

    im.show()
    # Finally, save it
    im.save("./frames1_new_bitmaps/vid-01-0000_new_bitmap_grey.png", "png")


generateBitmap(1)

# gaussian_noise = numpy.random.normal(loc = 50, scale = 10,size=(222,222))
# img = Image.fromarray(gaussian_noise,"YCbCr")
# img.save('MY_FUCKING_DICK.jpeg')
# img.show()
# print gaussian_noise