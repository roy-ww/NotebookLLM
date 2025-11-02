"use client"
import { useState, useEffect, useCallback } from "react"
import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger, DropdownMenuSeparator } from "@/components/ui/dropdown-menu"
import { Grid3X3, List, ChevronDown, Plus, Settings, MoreHorizontal, X, User, LogOut } from "lucide-react"
import Link from "next/link"
import { useAuth } from "@/contexts/AuthContext"
import { getWeChatAuthUrl, weChatLogin, getWeChatQrCode } from "@/lib/api"
import { useRouter } from "next/navigation"

interface Notebook {
  id: string
  title: string
  sources: number
  createdAt: string
  role: string
  icon?: string
  color?: string
}

const featuredNotebooks: Notebook[] = [
  {
    id: "1",
    title: "2025年全球大趋势",
    sources: 70,
    createdAt: "2025年7月7日",
    role: "Reader",
    icon: "E",
    color: "bg-red-600",
  },
  {
    id: "2",
    title: "科学迷的黄石游玩指南",
    sources: 17,
    createdAt: "2025年5月12日",
    role: "Reader",
    icon: "旅游",
    color: "bg-blue-600",
  },
  {
    id: "3",
    title: "威廉·莎士比亚：戏剧全集",
    sources: 45,
    createdAt: "2025年4月26日",
    role: "Reader",
    icon: "艺术与文化",
    color: "bg-amber-600",
  },
  {
    id: "4",
    title: "前50大企业的盈利报告",
    sources: 168,
    createdAt: "2025年4月18日",
    role: "Reader",
    icon: "商务",
    color: "bg-blue-500",
  },
]

const recentNotebooks: Notebook[] = [
  {
    id: "5",
    title: "TikTok Ban Drives Global Xiaohongshu Surge and Censorship Clash",
    sources: 6,
    createdAt: "2025年9月27日",
    role: "Owner",
    icon: "🇨🇳",
  },
  {
    id: "6",
    title: "AI's Second Half: Defining the Problems",
    sources: 6,
    createdAt: "2025年9月23日",
    role: "Owner",
    icon: "🤖",
  },
  {
    id: "7",
    title: "Untitled notebook",
    sources: 0,
    createdAt: "2025年9月23日",
    role: "Owner",
    icon: "📁",
  },
  {
    id: "8",
    title: "Untitled notebook",
    sources: 0,
    createdAt: "2025年9月23日",
    role: "Owner",
    icon: "📁",
  },
]

const featuredListNotebooks = [
  {
    id: "1",
    title: "数字时代的育儿建议",
    sources: 21,
    createdAt: "2025年5月6日",
    role: "Reader",
    icon: "⚫",
  },
  {
    id: "2",
    title: "《The Atlantic》的人生经营指南专栏",
    sources: 46,
    createdAt: "2025年4月23日",
    role: "Reader",
    icon: "🔴",
  },
  {
    id: "3",
    title: "威廉·莎士比亚：戏剧全集",
    sources: 45,
    createdAt: "2025年4月26日",
    role: "Reader",
    icon: "⚫",
  },
  {
    id: "4",
    title: "健康、财富和幸福趋势",
    sources: 24,
    createdAt: "2025年4月15日",
    role: "Reader",
    icon: "🔵",
  },
  {
    id: "5",
    title: "健康长寿秘诀",
    sources: 17,
    createdAt: "2025年5月6日",
    role: "Reader",
    icon: "⚫",
  },
]

export default function Homepage() {
  const [viewMode, setViewMode] = useState<"grid" | "list">("grid")
  const [showRenameModal, setShowRenameModal] = useState(false)
  const [selectedNotebook, setSelectedNotebook] = useState<Notebook | null>(null)
  const [renameValue, setRenameValue] = useState("")
  const [showLoginModal, setShowLoginModal] = useState(false)
  const [activeTab, setActiveTab] = useState<"all" | "my" | "featured">("all")
  const [isLoggingIn, setIsLoggingIn] = useState(false)
  const [qrCodeUrl, setQrCodeUrl] = useState<string | null>(null)
  const [qrCodeState, setQrCodeState] = useState<string | null>(null)
  const [isLoadingQrCode, setIsLoadingQrCode] = useState(false)
  
  const { user, login, logout, isAuthenticated } = useAuth()
  const router = useRouter()

  // 处理微信登录回调
  const handleWeChatCallback = useCallback(async (code: string, state: string | null) => {
    try {
      const response = await weChatLogin(code, state || undefined)
      
      if (response.success && response.data) {
        login(response.data.token, response.data.userInfo)
        // 清除URL中的code参数
        router.push('/')
        setShowLoginModal(false)
      } else {
        console.error('登录失败:', response.message)
        alert('登录失败: ' + response.message)
      }
    } catch (error) {
      console.error('登录错误:', error)
      alert('登录失败，请重试')
    } finally {
      setIsLoggingIn(false)
    }
  }, [login, router])

  // 处理微信回调（当用户扫码后，微信会跳转到redirectUri并带上code参数）
  useEffect(() => {
    // 从URL中获取code参数（兼容客户端渲染）
    if (typeof window !== 'undefined') {
      const urlParams = new URLSearchParams(window.location.search)
      const code = urlParams.get('code')
      const state = urlParams.get('state')
      
      // 如果URL中有code参数，说明用户已经扫码授权，需要处理登录
      if (code && !isAuthenticated && !isLoggingIn) {
        setIsLoggingIn(true)
        handleWeChatCallback(code, state)
      }
    }
  }, [isAuthenticated, isLoggingIn, handleWeChatCallback])

  // 当关闭登录模态框时，清除二维码状态
  useEffect(() => {
    if (!showLoginModal) {
      setQrCodeUrl(null)
      setQrCodeState(null)
    }
  }, [showLoginModal])

  const handleRename = (notebook: Notebook) => {
    setSelectedNotebook(notebook)
    setRenameValue(notebook.title)
    setShowRenameModal(true)
  }

  const handleSaveRename = () => {
    console.log("[v0] Renaming notebook:", selectedNotebook?.id, "to:", renameValue)
    setShowRenameModal(false)
    setSelectedNotebook(null)
    setRenameValue("")
  }

  const handleDelete = (notebook: Notebook) => {
    console.log("[v0] Deleting notebook:", notebook.id)
  }

  const handleWeChatLogin = async () => {
    try {
      setIsLoadingQrCode(true)
      const redirectUri = `${window.location.origin}/`
      const response = await getWeChatQrCode(redirectUri)
      
      if (response.success && response.data) {
        setQrCodeUrl(response.data.qrcodeUrl)
        setQrCodeState(response.data.state)
      } else {
        alert('获取微信二维码失败: ' + response.message)
      }
    } catch (error) {
      console.error('获取微信二维码错误:', error)
      alert('获取二维码失败，请重试')
    } finally {
      setIsLoadingQrCode(false)
    }
  }

  const handleLogout = () => {
    logout()
    router.push('/')
  }

  return (
    <div className="min-h-screen text-foreground dark" style={{ backgroundColor: "rgb(55, 56, 59)" }}>
      {/* Header */}
      <header className="flex items-center justify-between p-4 border-b border-border">
        <div className="flex items-center gap-3">
          <Link href="/" className="flex items-center gap-3">
            <div className="w-8 h-8 bg-gradient-to-br from-orange-500 to-red-500 rounded-full flex items-center justify-center">
              <span className="text-white text-sm font-bold">AI</span>
            </div>
            <h1 className="text-lg font-medium">NotebookLM</h1>
          </Link>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="ghost" size="sm">
            <Settings className="w-4 h-4" />
            设置
          </Button>
        
          {isAuthenticated && user ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="outline" size="sm" className="gap-2">
                  <Avatar className="w-6 h-6">
                    {user.avatarUrl ? (
                      <AvatarImage src={user.avatarUrl} alt={user.nickname} />
                    ) : null}
                    <AvatarFallback>
                      {user.nickname.charAt(0).toUpperCase()}
                    </AvatarFallback>
                  </Avatar>
                  <span>{user.nickname}</span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-56">
                <div className="px-2 py-1.5">
                  <p className="text-sm font-medium">{user.nickname}</p>
                  <p className="text-xs text-muted-foreground">{user.username}</p>
                </div>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={handleLogout}>
                  <LogOut className="w-4 h-4 mr-2" />
                  退出登录
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <Button variant="outline" size="sm" onClick={() => setShowLoginModal(true)}>
              <User className="w-4 h-4 mr-2" />
              登录/注册
            </Button>
          )}
        </div>
      </header>

      <div className="p-6">
        {/* Navigation Tabs */}
        <div className="flex items-center justify-between mb-8">
          <div className="flex items-center gap-6">
            <button
              className={`font-medium pb-2 ${activeTab === "all" ? "text-white border-b-2 border-white" : "text-muted-foreground hover:text-white"}`}
              onClick={() => setActiveTab("all")}
            >
              全部
            </button>
            <button
              className={`font-medium pb-2 ${activeTab === "my" ? "text-white border-b-2 border-white" : "text-muted-foreground hover:text-white"}`}
              onClick={() => setActiveTab("my")}
            >
              我的笔记本
            </button>
            <button
              className={`font-medium pb-2 ${activeTab === "featured" ? "text-white border-b-2 border-white" : "text-muted-foreground hover:text-white"}`}
              onClick={() => setActiveTab("featured")}
            >
              精选笔记本
            </button>
          </div>

          <div className="flex items-center gap-2">
            {/* View Toggle */}
            <div className="flex items-center bg-accent rounded-lg p-1">
              <Button
                variant={viewMode === "grid" ? "default" : "ghost"}
                size="sm"
                onClick={() => setViewMode("grid")}
                className="h-8 px-3"
              >
                <Grid3X3 className="w-4 h-4" />
              </Button>
              <Button
                variant={viewMode === "list" ? "default" : "ghost"}
                size="sm"
                onClick={() => setViewMode("list")}
                className="h-8 px-3"
              >
                <List className="w-4 h-4" />
              </Button>
            </div>

            <Button variant="outline" size="sm">
              最近 <ChevronDown className="w-4 h-4 ml-1" />
            </Button>

            <Button size="sm">
              <Plus className="w-4 h-4 mr-2" />
              新建
            </Button>
          </div>
        </div>

        {viewMode === "grid" ? (
          <>
            {(activeTab === "all" || activeTab === "featured") && (
              <div className="mb-8">
                <div className="flex items-center justify-between mb-4">
                  <h2 className="text-xl font-semibold">精选笔记本</h2>
                  <Button variant="ghost" size="sm">
                    查看全部 →
                  </Button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                  {featuredNotebooks.map((notebook) => (
                    <Link key={notebook.id} href="/notebook">
                      <Card
                        className="overflow-hidden hover:shadow-lg transition-shadow cursor-pointer group"
                        style={{ backgroundColor: "rgb(36, 38, 43)" }}
                      >
                        <div className="p-4">
                          <h3 className="font-medium text-sm mb-2 line-clamp-2 text-white">{notebook.title}</h3>
                          <p className="text-xs text-muted-foreground">
                            {notebook.createdAt} • {notebook.sources} 个来源
                          </p>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity"
                            onClick={(e) => {
                              e.preventDefault()
                              e.stopPropagation()
                            }}
                          >
                            <MoreHorizontal className="w-4 h-4 text-white" />
                          </Button>
                        </div>
                      </Card>
                    </Link>
                  ))}
                </div>
              </div>
            )}

            {(activeTab === "all" || activeTab === "my") && (
              <div>
                <h2 className="text-xl font-semibold mb-4">我的笔记本</h2>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                  <Card
                    className="border-2 border-dashed border-border hover:border-primary transition-colors cursor-pointer"
                    style={{ backgroundColor: "rgb(36, 38, 43)" }}
                  >
                    <div className="p-6 flex flex-col items-center justify-center text-center h-auto py-0">
                      <div className="w-12 h-12 bg-primary rounded-full flex items-center justify-center mb-3">
                        <Plus className="w-6 h-6 text-primary-foreground" />
                      </div>
                      <span className="font-medium text-white">新建笔记本</span>
                    </div>
                  </Card>

                  {recentNotebooks.map((notebook) => (
                    <Link key={notebook.id} href="/notebook">
                      <Card
                        className="hover:shadow-lg transition-shadow cursor-pointer group"
                        style={{ backgroundColor: "rgb(36, 38, 43)" }}
                      >
                        <div className="p-4 flex flex-col justify-between h-auto">
                          <div>
                            <h3 className="font-medium text-sm mb-2 line-clamp-2 text-white">{notebook.title}</h3>
                            <p className="text-xs text-muted-foreground">
                              {notebook.createdAt} • {notebook.sources} 个来源
                            </p>
                          </div>
                        </div>
                      </Card>
                    </Link>
                  ))}
                </div>
              </div>
            )}
          </>
        ) : (
          <>
            {(activeTab === "all" || activeTab === "featured") && (
              <div className="mb-8">
                <div className="flex items-center justify-between mb-4">
                  <h2 className="text-xl font-semibold">精选笔记本</h2>
                  <Button variant="ghost" size="sm">
                    查看全部 →
                  </Button>
                </div>

                <div
                  className="rounded-lg border border-border overflow-hidden"
                  style={{ backgroundColor: "rgb(36, 38, 43)" }}
                >
                  <div className="grid grid-cols-4 gap-4 p-4 border-b border-border text-sm font-medium text-muted-foreground">
                    <div>名称</div>
                    <div>来源</div>
                    <div>创建时间</div>
                    <div>角色</div>
                  </div>

                  {featuredListNotebooks.map((notebook) => (
                    <Link key={notebook.id} href="/notebook">
                      <div className="grid grid-cols-4 gap-4 p-4 border-b border-border last:border-b-0 hover:bg-accent/50 transition-colors group">
                        <div className="flex items-center gap-3">
                          <span className="font-medium text-sm text-white">{notebook.title}</span>
                        </div>
                        <div className="flex items-center text-sm text-muted-foreground">{notebook.sources} 个来源</div>
                        <div className="flex items-center text-sm text-muted-foreground">{notebook.createdAt}</div>
                        <div className="flex items-center justify-between">
                          <span className="text-sm text-muted-foreground">{notebook.role}</span>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="opacity-0 group-hover:opacity-100 transition-opacity"
                            onClick={(e) => {
                              e.preventDefault()
                              e.stopPropagation()
                            }}
                          >
                            <MoreHorizontal className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    </Link>
                  ))}
                </div>
              </div>
            )}

            {(activeTab === "all" || activeTab === "my") && (
              <div>
                <h2 className="text-xl font-semibold mb-4">我的笔记本</h2>

                <div
                  className="rounded-lg border border-border overflow-hidden"
                  style={{ backgroundColor: "rgb(36, 38, 43)" }}
                >
                  <div className="grid grid-cols-4 gap-4 p-4 border-b border-border text-sm font-medium text-muted-foreground">
                    <div>标题</div>
                    <div>来源</div>
                    <div>创建时间</div>
                    <div>角色</div>
                  </div>

                  {recentNotebooks.map((notebook) => (
                    <Link key={notebook.id} href="/notebook">
                      <div className="grid grid-cols-4 gap-4 p-4 border-b border-border last:border-b-0 hover:bg-accent/50 transition-colors group">
                        <div className="flex items-center gap-3">
                          <span className="font-medium text-sm text-white">{notebook.title}</span>
                        </div>
                        <div className="flex items-center text-sm text-muted-foreground">{notebook.sources} 个来源</div>
                        <div className="flex items-center text-sm text-muted-foreground">{notebook.createdAt}</div>
                        <div className="flex items-center justify-between">
                          <span className="text-sm text-muted-foreground">{notebook.role}</span>
                          <div className="relative">
                            <Button
                              variant="ghost"
                              size="sm"
                              className="opacity-0 group-hover:opacity-100 transition-opacity"
                              onClick={(e) => {
                                e.preventDefault()
                                e.stopPropagation()
                              }}
                            >
                              <MoreHorizontal className="w-4 h-4" />
                            </Button>
                          </div>
                        </div>
                      </div>
                    </Link>
                  ))}
                </div>
              </div>
            )}
          </>
        )}
      </div>

      {showLoginModal && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm">
          <div className="absolute inset-0 flex items-center justify-center p-4">
            <div
              className="rounded-lg border border-border w-full max-w-md overflow-hidden"
              style={{ backgroundColor: "rgb(35, 38, 43)" }}
            >
              {/* Header */}
              <div className="flex items-center justify-between p-6 border-b border-border">
                <h3 className="text-lg font-semibold">登录/注册</h3>
                <Button variant="ghost" size="sm" onClick={() => setShowLoginModal(false)}>
                  <X className="w-4 h-4" />
                </Button>
              </div>

              {/* Content */}
              <div className="p-6 text-center">
                {!qrCodeUrl ? (
                  <>
                    <div className="mb-6">
                      <div className="w-16 h-16 bg-green-500 rounded-full flex items-center justify-center mx-auto mb-4">
                        <svg className="w-8 h-8 text-white" viewBox="0 0 24 24" fill="currentColor">
                          <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.162 4.203 2.969 5.543.303.225.485.588.485.976 0 .669-.454 1.286-.454 1.286s1.202-.816 2.91-1.489c.325-.129.668-.086.945.043.839.39 1.787.649 2.836.649 4.8 0 8.691-3.288 8.691-7.342 0-4.054-3.891-7.342-8.691-7.342zm-.84 11.65c-.27 0-.489-.219-.489-.489v-1.956c0-.27.219-.489.489-.489s.489.219.489.489v1.956c0 .27-.219.489-.489.489zm4.8 0c-.27 0-.489-.219-.489-.489v-1.956c0-.27.219-.489.489-.489s.489.219.489.489v1.956c0 .27-.219.489-.489.489z" />
                        </svg>
                      </div>
                      <h4 className="text-lg font-medium mb-2">使用微信登录</h4>
                      <p className="text-sm text-muted-foreground">点击下方按钮生成二维码，使用微信扫码登录</p>
                    </div>

                    <Button 
                      className="w-full bg-green-500 hover:bg-green-600 text-white" 
                      onClick={handleWeChatLogin}
                      disabled={isLoadingQrCode}
                    >
                      <svg className="w-5 h-5 mr-2" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.162 4.203 2.969 5.543.303.225.485.588.485.976 0 .669-.454 1.286-.454 1.286s1.202-.816 2.91-1.489c.325-.129.668-.086.945.043.839.39 1.787.649 2.836.649 4.8 0 8.691-3.288 8.691-7.342 0-4.054-3.891-7.342-8.691-7.342zm-.84 11.65c-.27 0-.489-.219-.489-.489v-1.956c0-.27.219-.489.489-.489s.489.219.489.489v1.956c0 .27-.219.489-.489.489zm4.8 0c-.27 0-.489-.219-.489-.489v-1.956c0-.27.219-.489.489-.489s.489.219.489.489v1.956c0 .27-.219.489-.489.489z" />
                      </svg>
                      {isLoadingQrCode ? '生成中...' : '微信登录/注册'}
                    </Button>
                  </>
                ) : (
                  <>
                    <div className="mb-6">
                      <h4 className="text-lg font-medium mb-2">请使用微信扫码登录</h4>
                      <p className="text-sm text-muted-foreground mb-4">打开微信扫描下方二维码</p>
                      <div className="flex justify-center mb-4">
                        <div className="border-2 border-green-500 p-2 rounded-lg bg-white inline-block">
                          <img 
                            src={qrCodeUrl} 
                            alt="微信登录二维码" 
                            className="w-64 h-64"
                          />
                        </div>
                      </div>
                      <p className="text-xs text-muted-foreground">扫描后请在手机上确认登录</p>
                    </div>

                    <Button 
                      variant="outline" 
                      className="w-full" 
                      onClick={() => {
                        setQrCodeUrl(null)
                        setQrCodeState(null)
                      }}
                    >
                      重新生成二维码
                    </Button>
                  </>
                )}

                <p className="text-xs text-muted-foreground mt-4">登录即表示您同意我们的服务条款和隐私政策</p>
              </div>
            </div>
          </div>
        </div>
      )}

      {showRenameModal && selectedNotebook && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm">
          <div className="absolute inset-0 flex items-center justify-center p-4">
            <div
              className="rounded-lg border border-border w-full max-w-md overflow-hidden"
              style={{ backgroundColor: "rgb(35, 38, 43)" }}
            >
              {/* Header */}
              <div className="flex items-center justify-between p-6 border-b border-border">
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 bg-slate-600 rounded-full flex items-center justify-center">
                    <span className="text-2xl">{selectedNotebook.icon}</span>
                  </div>
                  <div>
                    <h3 className="font-semibold">重命名笔记本</h3>
                  </div>
                </div>
                <Button variant="ghost" size="sm" onClick={() => setShowRenameModal(false)}>
                  <X className="w-4 h-4" />
                </Button>
              </div>

              {/* Content */}
              <div className="p-6">
                <Input
                  value={renameValue}
                  onChange={(e) => setRenameValue(e.target.value)}
                  className="w-full"
                  placeholder="输入新名称"
                  autoFocus
                />
              </div>

              {/* Footer */}
              <div className="flex items-center justify-end gap-3 p-6 border-t border-border">
                <Button variant="outline" onClick={() => setShowRenameModal(false)}>
                  取消
                </Button>
                <Button onClick={handleSaveRename} disabled={!renameValue.trim()}>
                  保存
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
