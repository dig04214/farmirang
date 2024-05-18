import type { Metadata } from "next";
import Script from "next/script";
import "./globals.css";
import NavBar from "./Navbar";
import ChatButton from "./chatButton";

export const metadata: Metadata = {
  title: "팜이랑",
  description: "나만의 텃밭 도우미",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="kr">
      <body>
        <Script strategy="beforeInteractive" src={process.env.API_URL} />
        <div className="flex flex-col h-full">
          <NavBar />
          <div className="pt-[5rem] h-full">{children}</div>
          {/* <ChatButton /> */}
        </div>
      </body>
    </html>
  );
}
