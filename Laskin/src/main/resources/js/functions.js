
function max(a, b) {
    return Math.max(a, b);
}

function abs(a) {
    return Math.abs(a);
}

function fib(n) {
    if (n > 40) {
        return api.terminate("Calculating fibonacci n > 40 not allowed");
    }
    
   return n < 1 ? 0
        : n <= 2 ? 1
        : fib(n - 1) + fib(n - 2);
}