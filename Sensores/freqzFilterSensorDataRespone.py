from scipy import signal
import matplotlib.pyplot as plt
import numpy as np

#http://docs.scipy.org/doc/scipy/reference/generated/scipy.signal.freqz.html#scipy.signal.freqz
#b = signal.firwin(80, 0.5, window=('kaiser', 8))

#-------------------------Low Pass filter to sensor data----------------------
sp = 0.5  # Smoothing Parameter recommended values: 0.5>=factor>=0.02
#Low pass filer from Android Sensor programming
b = np.array([sp, (1-sp)])
a =1

#Hanning filter
#b = np.array([0.25, 0.5, 0.25])
#a =1

#High Pass filter Android Sensor book
#b = np.array([sp, -sp])
#a = np.array([1, -sp])

w, h = signal.freqz(b, a)  # compute the frequency response of a digital filter

fig_lpf = plt.figure()
plt.title('Digital filter frequency response')
ax1_lpf = fig_lpf.add_subplot(111)

plt.semilogy(w, np.abs(h), 'b')
plt.ylabel('Amplitude (dB)', color='b')
plt.xlabel('Frequency (rad/sample)')

ax2_lpf = ax1_lpf.twinx()
#angles_lpf = np.unwrap(np.angle(h)) # angle of the complex argument in radians
angles_lpf = np.rad2deg(np.unwrap(np.angle(h)))  # angle of the complex argument in degrees
plt.plot(w, angles_lpf, 'g')
plt.ylabel('Angle (Degrees)', color='g')
plt.grid()
plt.axis('tight')
plt.show()
#TODO: in the same figure, plot diferent values of factor (using legend of course)


