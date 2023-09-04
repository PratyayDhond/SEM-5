read -p "Enter the buying Price : " bp
read -p "Enter the Selling Price: " sp

profit=$(expr $sp - $bp)
if [ $profit -ge 0 ]; then
    echo "You made a profit of $profit\n."
else
    loss=$(( $profit * -1))
    echo "You incurred a loss of $loss\n."
fi