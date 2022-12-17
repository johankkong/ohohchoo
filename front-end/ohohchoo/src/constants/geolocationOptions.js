export const geolocationOptions = {
  enableHighAccuracy: true,
  timeout: 1000 * 60 * 1, // 1 min (1000 ms * 60sec * 1 minute = 60,000 ms)
  maximumAge: 1000 * 3600 * 24, // 24 hour
};