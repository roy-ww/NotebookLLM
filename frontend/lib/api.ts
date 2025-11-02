const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

export interface ApiResponse<T> {
  success: boolean
  message: string
  data?: T
}

export interface AuthResponse {
  token: string
  userInfo: {
    id: number
    username: string
    nickname: string
    avatarUrl?: string
    createdAt: string
    updatedAt: string
  }
}

export interface WeChatAuthUrlResponse {
  authUrl: string
  redirectUri: string
}

// 获取微信授权URL
export async function getWeChatAuthUrl(redirectUri?: string): Promise<ApiResponse<WeChatAuthUrlResponse>> {
  const url = new URL(`${API_BASE_URL}/auth/wechat/url`)
  if (redirectUri) {
    url.searchParams.append('redirectUri', redirectUri)
  }

  const response = await fetch(url.toString())
  return response.json()
}

// 微信登录
export async function weChatLogin(code: string, state?: string): Promise<ApiResponse<AuthResponse>> {
  const response = await fetch(`${API_BASE_URL}/auth/wechat/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ code, state }),
  })
  return response.json()
}

// 获取当前用户信息
export async function getCurrentUser(token: string): Promise<ApiResponse<AuthResponse['userInfo']>> {
  const response = await fetch(`${API_BASE_URL}/user/me`, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  })
  return response.json()
}

// 微信二维码相关接口
export interface WeChatQrCodeResponse {
  qrcodeUrl: string
  redirectUri: string
  state: string
}

// 获取微信登录二维码
export async function getWeChatQrCode(redirectUri?: string): Promise<ApiResponse<WeChatQrCodeResponse>> {
  const url = new URL(`${API_BASE_URL}/auth/wechat/qrcode`)
  if (redirectUri) {
    url.searchParams.append('redirectUri', redirectUri)
  }

  const response = await fetch(url.toString())
  return response.json()
}

