#ifndef CLAMP
#define CLAMP(x, low, high) (((x) > (high)) ? (high) : (((x) < (low)) ? (low) : (x)))
#define CLAMP0255(x) CLAMP(x, 0, 255)
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
