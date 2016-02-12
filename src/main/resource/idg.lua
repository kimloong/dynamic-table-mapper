local sequence = redis.call("INCR", ARGV[1])
local expire = redis.call("TTL", ARGV[1])
if expire == -1 then
    redis.call("EXPIRE", ARGV[1], ARGV[2])
    expire = ARGV[2]
end

return expire .. "," .. sequence