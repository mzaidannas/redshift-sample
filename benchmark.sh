start=`date +%s`
for i in {1..100}
do
   curl -fsSL -XGET http://localhost:8080
done
end=`date +%s`

runtime=$((end-start))
echo "100 requests took $runtime seconds"
