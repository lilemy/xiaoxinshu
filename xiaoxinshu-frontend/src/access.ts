/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(initialState: InitialState) {
  const { currentUser } = initialState ?? {};
  return {
    canUser: currentUser,
    canAdmin: currentUser?.userRole === 'admin',
  };
}
