/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'tailwindui.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'images.unsplash.com',
        pathname: '/**', 
      },
      {
        protocol: 'https',
        hostname: 'pub-39e7fe41840f42498dc3dc0995f0c666.r2.dev',
        pathname: '/**'
      },
    ],
  },
  reactStrictMode: false,
  env: {
    CUSTOM_URL: process.env.NEXT_PUBLIC_API_URL,
  },
};

module.exports = nextConfig;
