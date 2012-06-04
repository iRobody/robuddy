import android # See http://android-scripting.googlecode.com/
import gsd  # See http://getshitdone.googlecode.com/
import socket
import threading
import time


    
def feed(droid):
  droid.toggleBluetoothState(True)
  droid.bluetoothConnect('00001101-0000-1000-8000-00805F9B34FB', '00:06:66:04:b2:07')
  droid.bluetoothWrite('f')

class Server(gsd.App):
  def __init__(self, droid, webcam_url):
    self._droid = droid
    self._webcam_url = webcam_url

  def GET_(self, response):
    response.Render("""<html>
  <head>
  <title>Cat Feeder</title>
  </head>
  <body>
  <img src="%s">
  <br><a href="/feed">Feed!</a>
  </body>
  </html>""" % self._webcam_url)

  def GET_feed(self, response):
    feed(self._droid)
    response.Redirect('/')

def timer(droid):
  feedings = [4, 10, 20]
  last_feeding = None
  while True:
    hour = time.localtime().tm_hour
    if hour in feedings and hour != last_feeding:
      feed(droid)
      last_feeding = hour
    time.sleep(300)

def GetIpAddress():
  s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) 
  s.connect(('google.com', 0))
  return s.getsockname()[0]

def main():
  droid = android.Android()
  droid.wakeLockAcquirePartial()
  webcam_url = 'http://%s:%d/' % tuple(droid.webcamStart(5, 80).result)
  server = Server(droid, webcam_url)
  port = 8080
  base_url = 'http://%s:%d/' % (GetIpAddress(), port)
  threading.Thread(target=server.Serve, args=('0.0.0.0', port)).start()
  threading.Thread(target=timer, args=(droid,)).start()
  droid.notify('Cat Feeder', 'Running on: %s' % base_url)
  
if __name__ == '__main__':
  main()


