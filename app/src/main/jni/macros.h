#ifndef CLAMP
#define CLAMP(x,l,u) ((x)<(l)?(l):((x)>(u)?(u):(x)))
#define CLAMP0255(x) CLAMP(x,0,255)
#endif /* CLAMP */

#ifndef SQR
#define SQR(x) ((x) * (x))
#endif /* SQR */

#ifndef ROUND
#define ROUND(x) ((int) ((x) + 0.5))
#endif /* ROUND */

#ifndef MAX
#define MAX(a, b) ((a) > (b) ? (a) : (b))
#endif /* MAX */

#ifndef MIN
#define MIN(a, b) ((a) < (b) ? (a) : (b))
#endif /* MIN */



// source: https://code.csdn.net/snippets/336693/master/blog_20140509_1_6553357/raw

#ifndef MAKE_RGB565
#define RGB565_R(p) ((((p) & 0xF800) >> 11) << 3)
#define RGB565_G(p) ((((p) & 0x7E0 ) >> 5)  << 2)
#define RGB565_B(p) ( ((p) & 0x1F  )        << 3)
#define MAKE_RGB565(r,g,b) ((((r) >> 3) << 11) | (((g) >> 2) << 5) | ((b) >> 3))
#endif /* MAKE_RGB565 */

#ifndef MAKE_RGBA
#define RGBA_A(p) (((p) & 0xFF000000) >> 24)
#define RGBA_B(p) (((p) & 0x00FF0000) >> 16)
#define RGBA_G(p) (((p) & 0x0000FF00) >>  8)
#define RGBA_R(p)  ((p) & 0x000000FF)
#define MAKE_RGBA(r,g,b,a) (((a) << 24) | ((b) << 16) | ((g) << 8) | (r))
#endif /* MAKE_RGBA */
